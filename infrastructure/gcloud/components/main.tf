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

module "mongodb" {
  source                            = "./modules/mongodb"
  gcp_project_id                    = var.gcp_project_id
  gcp_region                        = var.gcp_region
  mongodb_version                   = var.mongodb_version
  mongodb_user_name_secret_name     = module.mongodb_user_name.name
  mongodb_user_password_secret_name = module.mongodb_user_password.name
}
