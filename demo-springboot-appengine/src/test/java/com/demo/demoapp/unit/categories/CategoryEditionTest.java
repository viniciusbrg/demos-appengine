package com.demo.demoapp.unit.categories;

import com.demo.demoapp.categories.CategoriesController;
import com.demo.demoapp.categories.CategoriesRepository;
import com.demo.demoapp.categories.Category;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class CategoryEditionTest {
  @MockBean
  CategoriesRepository categoriesRepository;

  @Autowired
  CategoriesController categoriesController;

  final static long categoryId = 1L;
  final Category existingCategory = new Category(categoryId, "a valid title", "aBcD18");
  final Category updatedCategory = new Category(categoryId, "a new title", "D18aBc");

  @Test
  public void validCategoryEditionTest() {
    Mockito.when(categoriesRepository.findById(categoryId))
        .thenReturn(Optional.of(existingCategory));
    Mockito.when(categoriesRepository.save(updatedCategory)).thenReturn(updatedCategory);

    var result = categoriesController.update(categoryId, updatedCategory);

    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertEquals(updatedCategory, result.getBody());
    Mockito.verify(categoriesRepository, Mockito.times(1)).findById(categoryId);
    Mockito.verify(categoriesRepository, Mockito.times(1)).save(updatedCategory);
  }

  @Test
  public void inexistentCategoryEditionTest() {
    Mockito.when(categoriesRepository.findById(categoryId)).thenReturn(Optional.empty());
    var result = categoriesController.update(categoryId, updatedCategory);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    Mockito.verify(categoriesRepository, Mockito.times(1)).findById(categoryId);
    Mockito.verify(categoriesRepository, Mockito.never()).save(Mockito.any());
  }

  @Test
  public void categoryChangingIdTest() {
    Mockito.when(categoriesRepository.findById(categoryId))
        .thenReturn(Optional.of(existingCategory));

    var result = categoriesController.update(categoryId,
        new Category(categoryId + 1, "a valid title", "123abc"));

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    Mockito.verify(categoriesRepository, Mockito.times(1)).findById(categoryId);
    Mockito.verify(categoriesRepository, Mockito.never()).save(Mockito.any());
  }

  @ParameterizedTest
  @MethodSource("invalidCategoriesProvider")
  public void invalidCategoryEditionTest(final Category invalidCategory) {
    Assertions.assertThrows(RuntimeException.class,
        () -> categoriesController.update(categoryId, invalidCategory));
    Mockito.verify(categoriesRepository, Mockito.never()).findById(categoryId);
    Mockito.verify(categoriesRepository, Mockito.never()).save(Mockito.any());
  }

  private static Stream<Arguments> invalidCategoriesProvider() {
    return Stream.of(
        Arguments.of(new Category(categoryId, "", "abcDEF")),
        Arguments.of(new Category(categoryId, null, "abcDEF")),
        Arguments.of(new Category(categoryId, "       ", "abcDEF")),
        Arguments.of(new Category(categoryId, "a valid one", "")),
        Arguments.of(new Category(categoryId, "a valid one", null)),
        Arguments.of(new Category(categoryId, "a valid one", "     ")),
        Arguments.of(new Category(categoryId, "a valid one", "aZZzXK")),
        Arguments.of(new Category(categoryId, "a valid one", "aBcd123")),
        Arguments.of(new Category(categoryId, "a valid one", "Abc12"))
    );
  }
}
