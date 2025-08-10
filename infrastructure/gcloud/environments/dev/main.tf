module "network" {
  source     = "../../components/network"
  gcp_region = local.gcp_region
}

module "app" {
  source              = "../../components/app"
  app_version         = "1.0.0"
  gcp_project_id      = local.gcp_project_id
  gcp_region          = local.gcp_region
  gcp_subnetwork_name = module.network.subnetwork_name
}

module "firestore" {
  source                = "../../components/firestore"
  gcp_project_id        = local.gcp_project_id
  gcp_region            = local.gcp_region
  service_account_email = module.app.service_account_email
}

module "github_actions" {
  source            = "../../components/github_actions"
  gcp_project_id    = local.gcp_project_id
  gcp_region        = local.gcp_region
  github_repository = "earelin/task-list"
  service_account_id = "gh-actions"
  sa_roles = [
    "roles/artifactregistry.writer",
    "roles/run.admin",
    "roles/iam.serviceAccountTokenCreator"
  ]
}
