terraform {
  required_version = ">= 1.11.0"
}

module "app" {
  source      = "modules/app"
  app_version = var.app_version
}

module "mongodb" {
  source    = "modules/mongodb"
}
