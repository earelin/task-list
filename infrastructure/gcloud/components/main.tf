terraform {
  required_version = ">= 1.11.0"
}

module "mongodb_user_name" {
  source         = "./modules/secret"
  gcp_region     = var.gcp_region
  gcp_project_id = var.gcp_project_id
  name           = "mongodb-user-name"
  value          = "root"
}

module "mongodb_user_password" {
  source         = "./modules/secret"
  gcp_region     = var.gcp_region
  gcp_project_id = var.gcp_project_id
  name           = "mongodb-user-password"
}
