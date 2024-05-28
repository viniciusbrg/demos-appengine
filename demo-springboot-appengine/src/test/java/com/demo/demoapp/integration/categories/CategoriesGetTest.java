package com.demo.demoapp.integration.categories;

import com.demo.demoapp.categories.CategoriesController;
import com.demo.demoapp.categories.Category;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class CategoriesGetTest {

  @Autowired
  CategoriesController categoriesController;

  final List<Category> rawCategories = new ArrayList<>(List.of(
      new Category(null, "Category 1", "abcDEF"),
      new Category(null, "Category 2", "1bcDEF"),
      new Category(null, "Category 2", "2bcDEF"),
      new Category(null, "Category 3", "3bcDEF"),
      new Category(null, "Category 4", "4bcDEF")
  ));

  final List<Category> createdCategories = new ArrayList<>();

  @BeforeEach
  void setUp() {
    rawCategories.add(new Category(1L, "LIVRE", "00FF00"));
    for (Category rawCategory : rawCategories) {
      createdCategories.add(categoriesController.create(rawCategory));
    }
  }

  public boolean findInList(Category category) {
    return rawCategories.stream().anyMatch(
        o -> o.getTitle().equals(category.getTitle()) && o.getColor().equals(category.getColor()));
  }

  @Test
  public void getAllTest() {
    final Iterable<Category> all = categoriesController.getAll();
    var out = new ArrayList<Category>();

    for (Category category : all) {
      out.add(category);
      Assertions.assertTrue(findInList(category));
    }

    Assertions.assertEquals(rawCategories.size(), out.size());
  }

  @Test
  public void getEachTest() {
    createdCategories.forEach(category -> {
      var response = categoriesController.getById(category.getId());

      Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
      final Category body = response.getBody();
      Assertions.assertEquals(category.getId(), body.getId());
      Assertions.assertEquals(category.getTitle(), body.getTitle());
      Assertions.assertEquals(category.getColor(), body.getColor());
    });
  }
}
