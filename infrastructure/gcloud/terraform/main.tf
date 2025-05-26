terraform {
  required_version = ">= 1.11.0"
}

resource "google_cloud_run_service" "default" {
  name     = "task-list-db"
  location = var.gcp_region

  template {
    spec {
      containers {
        image = "mongo:${var.mongodb_version}"
      }
    }
  }

  traffic {
    percent         = 100
    latest_revision = true
  }
}

