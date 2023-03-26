package com.mikhalov.taskonaut.repository;

import com.mikhalov.taskonaut.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotebookRepository extends JpaRepository<Notebook, String> {
}
