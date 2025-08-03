resource "google_firestore_database" "task_list_database" {
  project                     = var.gcp_project_id
  name                        = "(default)"
  location_id                 = var.gcp_region
  type                        = "FIRESTORE_NATIVE"
  concurrency_mode            = "OPTIMISTIC"
  app_engine_integration_mode = "DISABLED"

  delete_protection_state = "DELETE_PROTECTION_ENABLED"
}

resource "google_project_iam_member" "task_list_firestore_user" {
  project = var.gcp_project_id
  role    = "roles/datastore.user"
  member  = "serviceAccount:${google_service_account.task_list_service_identity.email}"
}
