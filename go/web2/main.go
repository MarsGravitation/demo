package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/gorilla/mux"
)

type people struct {
	ID   int    `json:"id"`
	Name string `json:"name"`
}

func YourHandler(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("Gorilla!\n"))
}

func ArticlesCategoryHandler(w http.ResponseWriter, r *http.Request) {
	// The names are used to create a map of route variables which can be
	// retrieved calling mux.Vars()
	vars := mux.Vars(r)
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, "Category: %v\n", vars["category"])
}

func PostPeople(w http.ResponseWriter, r *http.Request) {
	// params := mux.Vars(r)
	var p people
	_ = json.NewDecoder(r.Body).Decode(&p)
	// p.Name = params["name"]
	w.Write([]byte(p.Name))
}

func main() {
	dir, _ := os.Getwd()
	r := mux.NewRouter()
	// Routes consist of a path and a handler function.
	r.HandleFunc("/", YourHandler)
	r.HandleFunc("/articles/{category}/", ArticlesCategoryHandler)
	r.PathPrefix("/static/").Handler(http.StripPrefix("/static/", http.FileServer(http.Dir(dir))))
	r.HandleFunc("/people/{id}", PostPeople)

	// Bind to a port and pass our router in
	log.Fatal(http.ListenAndServe(":8000", r))
}
