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

resource "google_service_account" "task_list_service_identity" {
  account_id = "my-service-account"
}

resource "google_cloud_run_v2_service" "task_list_service" {
  name     = "task-list-db"
  location = var.gcp_region

  template {
    containers {
      image = "mongo:${var.mongodb_version}"
      env {
        name = "MONGO_INITDB_ROOT_USERNAME"
        value_source {
          secret_key_ref {
            secret  = module.mongodb_user_name.name
            version = "latest"
          }
        }
      }
      env {
        name = "MONGO_INITDB_ROOT_PASSWORD"
        value_source {
          secret_key_ref {
            secret  = module.mongodb_user_password.name
            version = "latest"
          }
        }
      }
      ports {
        container_port = 27017
      }
    }
    service_account = google_service_account.task_list_service_identity.email
  }

  traffic {
    percent = 100
    type    = "TRAFFIC_TARGET_ALLOCATION_TYPE_LATEST"
  }
}
