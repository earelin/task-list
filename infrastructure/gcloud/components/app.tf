resource "google_service_account" "task_list_service_identity" {
  account_id   = "task-list-service"
  display_name = "Task List Service Account"
}

resource "google_secret_manager_secret_iam_binding" "mongodb_user_name_binding" {
  project   = var.gcp_project_id
  secret_id = module.mongodb_user_name.name
  role      = "roles/secretmanager.secretAccessor"
  members = [
    "serviceAccount:${google_service_account.task_list_service_identity.email}"
  ]
}

resource "google_secret_manager_secret_iam_binding" "mongodb_user_password_binding" {
  project   = var.gcp_project_id
  secret_id = module.mongodb_user_password.name
  role      = "roles/secretmanager.secretAccessor"
  members = [
    "serviceAccount:${google_service_account.task_list_service_identity.email}"
  ]
}

resource "google_artifact_registry_repository" "task_list_repository" {
  location      = var.gcp_region
  repository_id = "task-list"
  description   = "Task List Application Docker Repository"
  format        = "DOCKER"

  docker_config {
    immutable_tags = true
  }
}

resource "google_artifact_registry_repository_iam_binding" "task_list_repository_binding" {
  project    = var.gcp_project_id
  location   = var.gcp_region
  repository = google_artifact_registry_repository.task_list_repository.name
  role       = "roles/artifactregistry.reader"
  members = [
    "serviceAccount:${google_service_account.task_list_service_identity.email}"
  ]
}

resource "google_cloud_run_v2_service" "task_list_service" {
  name                = "task-list"
  location            = var.gcp_region
  deletion_protection = false

  template {
    containers {
      image = "${var.gcp_region}-docker.pkg.dev/${var.gcp_project_id}/${google_artifact_registry_repository.task_list_repository.repository_id}/task-list-app:${var.app_version}"
      env {
        name = "SPRING_DATA_MONGODB_USERNAME"
        value_source {
          secret_key_ref {
            secret  = module.mongodb_user_name.name
            version = "latest"
          }
        }
      }
      env {
        name = "SPRING_DATA_MONGODB_PASSWORD"
        value_source {
          secret_key_ref {
            secret  = module.mongodb_user_password.name
            version = "latest"
          }
        }
      }
      env {
        name  = "LOGBACK_APPENDER"
        value = "CONSOLE_GCP"
      }
      resources {
        limits = {
          cpu    = "1"
          memory = "1024Mi"
        }
      }
      ports {
        container_port = 8080
      }
    }

    vpc_access {
      network_interfaces {
        subnetwork = google_compute_subnetwork.task_list_subnetwork.name
        tags       = ["http"]
      }
    }

    service_account = google_service_account.task_list_service_identity.email
  }

  traffic {
    percent = 100
    type    = "TRAFFIC_TARGET_ALLOCATION_TYPE_LATEST"
  }
}
