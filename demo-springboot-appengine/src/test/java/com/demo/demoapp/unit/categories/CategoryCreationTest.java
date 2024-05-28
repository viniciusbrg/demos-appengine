package com.demo.demoapp.unit.categories;

import com.demo.demoapp.categories.CategoriesController;
import com.demo.demoapp.categories.CategoriesRepository;
import com.demo.demoapp.categories.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CategoryCreationTest {
  @MockBean
  CategoriesRepository categoriesRepository;

  @Autowired
  CategoriesController categoriesController;

  @Test
  public void createValidCategoryTest() {
    final Category categoryToCreate = new Category(null, "valid title", "012ABC");
    final Category createdCategory = new Category(1L, "valid title", "012ABC");
    Mockito.when(categoriesRepository.save(categoryToCreate)).thenReturn(createdCategory);

    var result = categoriesController.create(categoryToCreate);

    Mockito.verify(categoriesRepository, Mockito.times(1)).save(categoryToCreate);
    Assertions.assertEquals(createdCategory, result);
  }

  @Test
  public void createValidCategoryColorUpperLowerTest() {
    final Category categoryToCreate = new Category(null, "valid title", "A12aBc");
    final Category createdCategory = new Category(1L, "valid title", "A12aBc");
    Mockito.when(categoriesRepository.save(categoryToCreate)).thenReturn(createdCategory);

    var result = categoriesController.create(categoryToCreate);

    Mockito.verify(categoriesRepository, Mockito.times(1)).save(categoryToCreate);
    Assertions.assertEquals(createdCategory, result);
  }

  void verifyInvalidCategoryCreation(final Category invalidCategory) {
    Assertions.assertThrows(RuntimeException.class,
        () -> categoriesController.create(invalidCategory));
    Mockito.verify(categoriesRepository, Mockito.never()).save(Mockito.any());
  }

  @Test
  public void categoryWithoutTitle() {
    verifyInvalidCategoryCreation(new Category(null, "", "012abc"));
  }

  @Test
  public void categoryNullTitle() {
    verifyInvalidCategoryCreation(new Category(null, null, "012abc"));
  }

  @Test
  public void categoryBlankTitle() {
    verifyInvalidCategoryCreation(new Category(null, "     ", "012abc"));
  }

  @Test
  public void categoryWithoutColor() {
    verifyInvalidCategoryCreation(new Category(null, "a valid title", ""));
  }

  @Test
  public void categoryNullColor() {
    verifyInvalidCategoryCreation(new Category(null, "a valid title", null));
  }

  @Test
  public void categoryBlankColor() {
    verifyInvalidCategoryCreation(new Category(null, "a valid title", "      "));
  }

  @Test
  public void categoryInvalidHexColor() {
    verifyInvalidCategoryCreation(new Category(null, "a valid title", "aZZzXK"));
  }

  @Test
  public void categoryHexWithMoreThan6Digits() {
    verifyInvalidCategoryCreation(new Category(null, "a valid title", "aBcd123"));
  }


  @Test
  public void categoryHexWithLessThan6Digits() {
    verifyInvalidCategoryCreation(new Category(null, "a valid title", "Abc12"));
  }
}
