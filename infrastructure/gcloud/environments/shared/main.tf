module "github_actions" {
  source             = "../../components/github_actions"
  gcp_project_id     = local.gcp_project_id
  gcp_region         = local.gcp_region
  github_repository  = "earelin/task-list"
  service_account_id = "gh-actions"
  sa_roles = [
    "roles/artifactregistry.writer",
    "roles/run.admin",
    "roles/iam.serviceAccountTokenCreator"
  ]
}
