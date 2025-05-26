provider "google" {
  project = var.gcp_project_id
  region  = var.gcp_region
}

provider "random" {}

terraform {
  required_providers {
    google = {
      source  = "kreuzwerker/docker"
      version = "~> 3.5.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.7.2"
    }
  }
}
