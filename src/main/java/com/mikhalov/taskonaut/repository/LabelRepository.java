package com.mikhalov.taskonaut.repository;

import com.mikhalov.taskonaut.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<Label, String> {
}
