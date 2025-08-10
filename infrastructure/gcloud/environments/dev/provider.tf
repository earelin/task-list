terraform {
  backend "gcs" {
    bucket = "task-list-dev-tf-state-bucket"
    prefix = "terraform/state"
  }

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 6.47"
    }
  }
}

provider "google" {
  project = local.gcp_project_id
  region  = local.gcp_region
}
