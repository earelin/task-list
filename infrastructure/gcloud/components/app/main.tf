resource "google_service_account" "task_list_service_identity" {
  account_id   = "task-list-service"
  display_name = "Task List Service Account"
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
      image = "nginx:1.29.0"
      env {
        name  = "LOGBACK_APPENDER"
        value = "CONSOLE_GCP"
      }
      env {
        name  = "SPRING_CLOUD_GCP_FIRESTORE_EMULATOR_ENABLED"
        value = "false"
      }
      resources {
        limits = {
          cpu    = "1"
          memory = "1024Mi"
        }
      }
      ports {
        container_port = 80
      }
    }

    vpc_access {
      network_interfaces {
        subnetwork = var.gcp_subnetwork_name
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
