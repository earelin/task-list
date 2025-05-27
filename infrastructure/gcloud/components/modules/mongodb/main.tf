resource "google_service_account" "mongodb_service_identity" {
  account_id   = "mongodb-service"
  display_name = "MongoDB Service Account"
}

resource "google_secret_manager_secret_iam_binding" "mongodb_service_mongodb_user_name_binding" {
  project   = var.gcp_project_id
  secret_id = var.mongodb_user_name_secret_name
  role      = "roles/secretmanager.secretAccessor"
  members = [
    "serviceAccount:${google_service_account.mongodb_service_identity.email}"
  ]
}

resource "google_secret_manager_secret_iam_binding" "mongodb_service_mongodb_user_password_binding" {
  project   = var.gcp_project_id
  secret_id = var.mongodb_user_password_secret_name
  role      = "roles/secretmanager.secretAccessor"
  members = [
    "serviceAccount:${google_service_account.mongodb_service_identity.email}"
  ]
}

resource "google_compute_instance" "mongodb_instance" {
	name         = "mongodb"
	machine_type = "e2-micro"
	zone         = "${var.gcp_region}-a"

	allow_stopping_for_update = true

	boot_disk {
		initialize_params {
			image = "debian-cloud/debian-12"
		}
	}

	metadata = {
    user-data = file("${path.module}/cloud-config.yaml")		
  }

	network_interface {
		network = "default"
	}

	scheduling {
		preemptible                 = true
    automatic_restart           = false
    provisioning_model          = "SPOT"
    instance_termination_action = "STOP"
	}

	service_account {
		email  = google_service_account.mongodb_service_identity.email
		scopes = ["cloud-platform"]
	}
}
