terraform {
  backend "gcs" {
    bucket = "task-list-dev-tf-state-bucket"
    prefix = "terraform/state"
  }
}
