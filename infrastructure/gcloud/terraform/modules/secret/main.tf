resource "google_secret_manager_secret" "secret" {
  secret_id = var.secret_id

  labels = {
    label = var.label
  }

  replication {
    user_managed {
      replicas {
        location = var.gcp_region
      }
    }
  }
}

resource "random_password" "password-generator" {
  length  = 20
  special = false
  upper   = true
  lower   = true
  numeric = true
}

resource "google_secret_manager_secret_version" "secret_initial_version" {
  secret = google_secret_manager_secret.secret.id
  secret_data = var.value == "" ? random_password.password-generator.result : var.value
}
