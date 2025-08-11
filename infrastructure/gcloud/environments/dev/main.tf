module "app" {
  source                              = "../../components/app"
  app_version                         = "1.0.0"
  gcp_project_id                      = local.gcp_project_id
  gcp_region                          = local.gcp_region
  gcp_subnetwork_name                 = var.shared_subnetwork_name
  github_actions_service_account_email = var.github_actions_service_account_email
}

module "firestore" {
  source                = "../../components/firestore"
  gcp_project_id        = local.gcp_project_id
  gcp_region            = local.gcp_region
  service_account_email = module.app.service_account_email
}
