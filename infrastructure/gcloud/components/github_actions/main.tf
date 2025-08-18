resource "google_service_account" "github_actions" {
  account_id   = var.service_account_id
  display_name = "GitHub Actions Federated SA"
  project      = var.gcp_project_id
}

resource "google_artifact_registry_repository_iam_member" "task_list_repository_github_actions_writer" {
  count      = google_service_account.github_actions.email == null ? 0 : 1
  project    = var.gcp_project_id
  location   = var.gcp_region
  repository = var.task_list_repository_name
  role       = "roles/artifactregistry.writer"
  member     = "serviceAccount:${google_service_account.github_actions.email}"
}
