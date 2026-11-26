const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_uid_123";
const BOB_UID = "bob_uid_456";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read or write profiles, inquiries, or bookmarks", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  const now = new Date(Date.now() - 1000);

  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("inquiries").get()
  );
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("bookmarks").get()
  );
  await assertFails(
    unauthDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("inquiries")
      .doc("inq_1")
      .set({
        userId: ALICE_UID,
        customerName: "Rahul",
        phone: "9179818444",
        serviceName: "PAN Card Apply",
        status: "OPEN",
        createdAt: now,
        updatedAt: now,
      })
  );
});

test("Authenticated owner can create and update UserProfile; cross-user and ghost fields fail", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const created = new Date(Date.now() - 5000);

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Rahul Sharma",
      email: "rahul@example.com",
      alertsEnabled: false,
      createdAt: created,
      updatedAt: created,
    })
  );

  // Partial update allowed
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).update({
      alertsEnabled: true,
      updatedAt: new Date(Date.now() - 1000),
    })
  );

  // Ghost field / shadow update rejected
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      isAdmin: true,
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Immutable createdAt mutation rejected
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      createdAt: new Date(Date.now() - 500),
      updatedAt: new Date(Date.now() - 500),
    })
  );

  // Cross-user read & write rejected
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).update({
      alertsEnabled: false,
      updatedAt: new Date(Date.now() - 500),
    })
  );
});

test("ServiceInquiry Kotlin-aligned create, update, terminal lock, and delete", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const created = new Date(Date.now() - 10000);

  // Valid create (no @DocumentId `id` field, status OPEN)
  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("inquiries")
      .doc("inq_1")
      .set({
        userId: ALICE_UID,
        customerName: "Rahul Sharma",
        phone: "9179818444",
        serviceName: "New PAN Card Apply",
        message: "Need urgent PAN card update",
        status: "OPEN",
        createdAt: created,
        updatedAt: created,
      })
  );

  // Reject extra `id` field matching @DocumentId trap
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("inquiries")
      .doc("inq_2")
      .set({
        id: "inq_2",
        userId: ALICE_UID,
        customerName: "Rahul Sharma",
        phone: "9179818444",
        serviceName: "New PAN Card Apply",
        status: "OPEN",
        createdAt: created,
        updatedAt: created,
      })
  );

  // Reject future timestamp
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("inquiries")
      .doc("inq_3")
      .set({
        userId: ALICE_UID,
        customerName: "Rahul Sharma",
        phone: "9179818444",
        serviceName: "New PAN Card Apply",
        status: "OPEN",
        createdAt: new Date(Date.now() + 60000),
        updatedAt: created,
      })
  );

  // List subcollection succeeds for owner, fails for Bob
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("inquiries").get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("inquiries").get()
  );

  // Owner updates status to COMPLETED
  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("inquiries")
      .doc("inq_1")
      .update({
        status: "COMPLETED",
        updatedAt: new Date(Date.now() - 1000),
      })
  );

  // Terminal state lock: once COMPLETED, further updates fail
  await assertFails(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("inquiries")
      .doc("inq_1")
      .update({
        status: "OPEN",
        updatedAt: new Date(Date.now() - 500),
      })
  );

  // Owner can delete their own inquiry; Bob cannot
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("inquiries")
      .doc("inq_1")
      .delete()
  );
  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("inquiries")
      .doc("inq_1")
      .delete()
  );
});

test("SavedBookmark Kotlin-aligned create, list, and delete with cross-user isolation", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const created = new Date(Date.now() - 5000);

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("bookmarks")
      .doc("svc_pan_apply")
      .set({
        userId: ALICE_UID,
        itemTitle: "New PAN Card Apply",
        categoryTitle: "PAN Card Services",
        itemType: "SERVICE",
        createdAt: created,
        updatedAt: created,
      })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("bookmarks").get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("bookmarks").get()
  );
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("bookmarks")
      .doc("svc_pan_apply")
      .delete()
  );
  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("bookmarks")
      .doc("svc_pan_apply")
      .delete()
  );
});
