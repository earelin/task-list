terraform {
  required_version = ">= 1.11.0"
}

module "mongodb_user_name" {
  source         = "./modules/secret"
  gcp_region     = var.gcp_region
  gcp_project_id = var.gcp_project_id
  label          = "MongoDB user name"
  secret_id      = "mongodb-user-name"
  value          = "task-list"
}

module "mongodb_user_password" {
  source         = "./modules/secret"
  gcp_region     = var.gcp_region
  gcp_project_id = var.gcp_project_id
  label          = "MongoDB user password"
  secret_id      = "mongodb-user-password"
}

resource "google_cloud_run_service" "default" {
  name     = "task-list-db"
  location = var.gcp_region

  template {
    spec {
      containers {
        image = "mongo:${var.mongodb_version}"
        env {
          name = "MONGO_INITDB_ROOT_USERNAME"
          value_from {
            secret_key_ref {
              name = module.mongodb_user_name.secret_name
              key  = "latest"
            }
          }
        }
        env {
          name = "MONGO_INITDB_ROOT_PASSWORD"
          value_from {
            secret_key_ref {
              name = module.mongodb_user_password.secret_name
              key  = "latest"
            }
          }
        }
        ports {
          container_port = 27017
        }
      }
    }
  }

  traffic {
    percent         = 100
    latest_revision = true
  }
}
