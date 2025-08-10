resource "google_service_account" "github_actions" {
  account_id   = var.service_account_id
  display_name = "GitHub Actions Federated SA"
  project      = var.gcp_project_id
}
