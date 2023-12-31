package com.vytsablinskas.flavorfare.integration.services;

import com.vytsablinskas.flavorfare.business.exceptions.ResourceNotFoundException;
import com.vytsablinskas.flavorfare.business.services.interfaces.RestaurantService;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.RestaurantDto;
import com.vytsablinskas.flavorfare.shared.dtos.restaurant.UpdateRestaurantDto;
import com.vytsablinskas.flavorfare.utils.RestaurantTestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestaurantServiceIntegrationTests {
    private final RestaurantService underTest;

    @Autowired
    public RestaurantServiceIntegrationTests(RestaurantService underTest) {
        this.underTest = underTest;
    }

    @Test
    public void getRestaurants_shouldGetAllRestaurants() {
        underTest.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        underTest.addRestaurant(RestaurantTestData.getAddRestaurantDtoB());

        List<RestaurantDto> restaurants = underTest.getRestaurants();

        assertThat(restaurants)
                .hasSize(2);
    }

    @Test
    public void getRestaurant_validId_returnsRestaurantDto() {
        RestaurantDto restaurantDto = underTest.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        RestaurantDto result = underTest.getRestaurant(restaurantDto.getId());

        assertThat(result)
                .isNotNull()
                .isEqualTo(restaurantDto);
    }

    @Test
    public void getRestaurant_invalidId_returnsThrowResourceNotFoundException() {
        Assertions.assertThatThrownBy(() -> underTest.getRestaurant(1))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void addRestaurant_shouldAddRestaurantWithAutoGeneratedId() {
        RestaurantDto createdRestaurant = underTest.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        assertThat(createdRestaurant.getId()).isGreaterThan(0);
    }

    @Test
    public void updateRestaurant_validId_shouldUpdateAndReturnUpdatedRestaurant() {
        RestaurantDto createdRestaurant = underTest.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());
        UpdateRestaurantDto restaurantUpdateDto = RestaurantTestData.getUpdateRestaurantDtoA();

        RestaurantDto result = underTest.updateRestaurant(createdRestaurant.getId(), restaurantUpdateDto);
        assertThat(result.getName()).isEqualTo(restaurantUpdateDto.getName());
    }

    @Test
    public void updateRestaurant_invalidId_shouldThrowResourceNotFoundException() {
        Assertions.assertThatThrownBy(() -> underTest.updateRestaurant(1, RestaurantTestData.getUpdateRestaurantDtoA()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void deleteRestaurant_validId_shouldDeleteRestaurant() {
        RestaurantDto createdRestaurantA = underTest.addRestaurant(RestaurantTestData.getAddRestaurantDtoA());

        underTest.deleteRestaurant(createdRestaurantA.getId());
        List<RestaurantDto> restaurants = underTest.getRestaurants();

        assertThat(restaurants).hasSize(0);
    }

    @Test
    public void deleteRestaurant_invalidId_shouldThrowResourceNotFoundException() {
        Integer invalidId = 1;

        Assertions.assertThatThrownBy(() -> underTest.getRestaurant(invalidId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}