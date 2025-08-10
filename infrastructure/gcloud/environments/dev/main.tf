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
