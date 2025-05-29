terraform {
  backend "gcs" {
    bucket = "task-list-prod-tf-state-bucket"
    prefix = "terraform/state"
  }
}
