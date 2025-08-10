resource "google_service_account" "github_actions" {
  account_id   = var.service_account_id
  display_name = "GitHub Actions Federated SA"
  project      = var.gcp_project_id
}

# Workload Identity Pool (one per project if not existing)
resource "google_iam_workload_identity_pool" "github_actions" {
  workload_identity_pool_id = "github-actions-pool"
  display_name              = "GitHub OIDC Pool"
  description               = "Pool for GitHub Actions direct workload identity federation"
  project                   = var.gcp_project_id
}

# Workload Identity Pool Provider for GitHub
resource "google_iam_workload_identity_pool_provider" "github_actions" {
  workload_identity_pool_id          = google_iam_workload_identity_pool.github_actions.workload_identity_pool_id
  workload_identity_pool_provider_id = "github-actions-provider"
  display_name                       = "GitHub Provider"
  description                        = "Trust configuration for GitHub OIDC tokens"
  project                            = var.gcp_project_id

  oidc {
    issuer_uri = "https://token.actions.githubusercontent.com"
  }

  attribute_mapping = {
    "google.subject"       = "assertion.sub"
    "attribute.actor"      = "assertion.actor"
    "attribute.aud"        = "assertion.aud"
    "attribute.repository" = "assertion.repository"
    "attribute.ref"        = "assertion.ref"
  }

  attribute_condition = "assertion.repository == '${var.github_repository}' && assertion.ref == 'refs/heads/trunk'"
}

# Bind service account to Workload Identity Pool for specific repo and branch
resource "google_service_account_iam_member" "wif_binding" {
  service_account_id = google_service_account.github_actions.name
  role               = "roles/iam.workloadIdentityUser"
  member             = "principalSet://iam.googleapis.com/${google_iam_workload_identity_pool.github_actions.name}/attribute.repository/${var.github_repository}"
  
  depends_on = [google_iam_workload_identity_pool_provider.github_actions]
}

# Project-level role bindings
resource "google_project_iam_member" "sa_roles" {
  for_each = toset(var.sa_roles)
  project  = var.gcp_project_id
  role     = each.value
  member   = "serviceAccount:${google_service_account.github_actions.email}"
}
