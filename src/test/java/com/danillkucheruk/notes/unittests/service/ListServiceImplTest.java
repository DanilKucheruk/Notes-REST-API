package com.danillkucheruk.notes.unittests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.danillkucheruk.notes.model.User;
import com.danillkucheruk.notes.dto.ListCreateEditDto;
import com.danillkucheruk.notes.dto.ListDto;
import com.danillkucheruk.notes.mapper.ListCreateEditMapper;
import com.danillkucheruk.notes.mapper.ListMapper;
import com.danillkucheruk.notes.model.ListEntity;
import com.danillkucheruk.notes.repository.ListRepository;
import com.danillkucheruk.notes.service.UserService;
import com.danillkucheruk.notes.service.impl.ListServiceImpl;


@ExtendWith(MockitoExtension.class)
public class ListServiceImplTest {

    @InjectMocks
    private ListServiceImpl listService;

    @Mock
    private ListRepository listRepository;

    @Mock
    private ListCreateEditMapper createEditListMapper;

    @Mock
    private ListMapper listMapper;

    @Mock
    private UserService userService;

    private static final Long LIST_ID = 1L;
    private static final String USERNAME = "username";
    private static final User DEF_USER = new User(USERNAME, null, Collections.emptyList());
    private static final ListEntity DEF_LIST = new ListEntity(null,null, DEF_USER,Collections.emptyList());
    private static final ListDto DEF_LIST_DTO = new ListDto(LIST_ID,null,null,null,Collections.emptyList());


    @Test
    public void findById_ShouldReturnListDto_WhenListExistsAndBelongsToUser() {
        // given
        ListEntity listEntity = DEF_LIST;
        ListDto listDto = DEF_LIST_DTO;
        when(listRepository.findById(LIST_ID)).thenReturn(Optional.of(listEntity));
        when(listMapper.map(listEntity)).thenReturn(listDto);

        // when
        Optional<ListDto> result = listService.findById(LIST_ID, USERNAME);

        // then
        assertTrue(result.isPresent());
        assertEquals(listDto, result.get());
    }
    
    
    @Test
    public void findById_ShouldReturnEmptyOptional_WhenListDoesNotExist() {
        // given
        when(listRepository.findById(LIST_ID)).thenReturn(Optional.empty());

        // where
        Optional<ListDto> result = listService.findById(LIST_ID, USERNAME);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void findById_ShouldReturnEmptyOptional_WhenListBelongsToAnotherUser() {
        // given
        String anotherUsername = "anotherUser";
        ListEntity listEntity = new ListEntity();
        listEntity.setId(LIST_ID);
        listEntity.setUser(new User(anotherUsername, null, Collections.emptyList()));
    
        when(listRepository.findById(LIST_ID)).thenReturn(Optional.of(listEntity));
    
        // where
        Optional<ListDto> result = listService.findById(LIST_ID, USERNAME);
    
        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void findById_ShouldReturnEmptyOptional_WhenListIdIsNull() {
        // when
        Optional<ListDto> result = listService.findById(null, USERNAME);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAll_ShouldReturnEmptyList_WhenNoListsBelongToUser() {
        // given
        when(listRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<ListDto> result = listService.findAll(USERNAME);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void findAll_ShouldReturnListsBelongingToUser() {
        // given
        ListEntity listEntity1 = DEF_LIST;
        listEntity1.setUser(new User(USERNAME, null, Collections.emptyList()));
        ListEntity listEntity2 = new ListEntity();
        listEntity2.setId(2L);
        listEntity2.setUser(new User(USERNAME, null, Collections.emptyList()));

        List<ListEntity> listEntities = Arrays.asList(listEntity1, listEntity2);

        when(listRepository.findAll()).thenReturn(listEntities);

        List<ListDto> expectedListDtos = Arrays.asList(new ListDto(), new ListDto());

        when(listMapper.map(listEntity1)).thenReturn(expectedListDtos.get(0));
        when(listMapper.map(listEntity2)).thenReturn(expectedListDtos.get(1));

        // when
        List<ListDto> result = listService.findAll(USERNAME);

        // then
        assertEquals(expectedListDtos, result);
    }

    
    @Test
    public void delete_ShouldReturnTrue_WhenListExistsAndBelongsToUser() {
        // given
        ListEntity listEntity = DEF_LIST;
        when(listRepository.findById(1L)).thenReturn(Optional.of(listEntity));

        // when
        boolean result = listService.delete(1L, USERNAME);

        // then
        assertTrue(result);
        verify(listRepository).delete(listEntity);
        verify(listRepository).flush();
    }

    @Test
    public void delete_ShouldReturnFalse_WhenListDoesNotExist() {
        // given
        when(listRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        boolean result = listService.delete(1L, USERNAME);

        // then
        assertFalse(result);
        verify(listRepository, never()).delete(any());
        verify(listRepository, never()).flush();
    }

    @Test
    public void delete_ShouldReturnFalse_WhenListExistsButDoesNotBelongToUser() {
        // given
        String username = "anotherUser";
        ListEntity listEntity = DEF_LIST;
        when(listRepository.findById(1L)).thenReturn(Optional.of(listEntity));

        // when
        boolean result = listService.delete(1L, username);

        // then
        assertFalse(result);
        verify(listRepository, never()).delete(any());
        verify(listRepository, never()).flush();
    }

    @Test
    public void findByUserId_ShouldReturnListsBelongingToUser() {
        // given
        Long userId = 1L;
        ListEntity listEntity1 = DEF_LIST;

        ListEntity listEntity2 = DEF_LIST;

        List<ListEntity> listEntities = Arrays.asList(listEntity1, listEntity2);
        when(listRepository.findByUserId(userId)).thenReturn(listEntities);

        // when
        List<ListEntity> result = listService.findByUserId(userId);

        // then
        assertEquals(listEntities, result);
    }

    @Test
    public void createNewList_ShouldThrowException_WhenUserDoesNotExist() {
        ListCreateEditDto listDto = new ListCreateEditDto("testList", "testDiscr");
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> listService.createNewList(listDto, USERNAME));
    }

    @Test
    public void createNewList_ShouldReturnListDto_WhenListDtoIsValidsa() {
        // Arrange
        ListCreateEditDto listDto = new ListCreateEditDto();
        listDto.setTitle("testTitle");
        listDto.setDescription("testDescription");

        User user = DEF_USER;

        ListEntity listEntity = new ListEntity();
        listEntity.setTitle(listDto.getTitle());
        listEntity.setDescription(listDto.getDescription());
        listEntity.setUser(user);

        ListDto expectedListDto = new ListDto();
        expectedListDto.setTitle(listDto.getTitle());
        expectedListDto.setDescription(listDto.getDescription());
        expectedListDto.setUserId(user.getId());

        when(createEditListMapper.map(listDto)).thenReturn(listEntity);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(listRepository.save(any(ListEntity.class))).thenReturn(listEntity);
        when(listMapper.map(any(ListEntity.class))).thenReturn(expectedListDto);

        // Act
        ListDto actualListDto = listService.createNewList(listDto, user.getUsername());

        // Assert
        assertEquals(expectedListDto, actualListDto);
        verify(createEditListMapper, times(1)).map(listDto);
        verify(userService, times(1)).findByUsername(user.getUsername());
        verify(listRepository, times(1)).save(listEntity);
        verify(listMapper, times(1)).map(listEntity);
    }

    @Test
    public void update_ShouldReturnUpdatedListDto_WhenIdAndListDtoAreValid() {
        // Arrange
        ListCreateEditDto listDto = new ListCreateEditDto();
        listDto.setTitle("updatedTitle");
        listDto.setDescription("updatedDescription");

        ListEntity oldListEntity = DEF_LIST;
        oldListEntity.setTitle("oldTitle");
        oldListEntity.setDescription("oldDescription");

        ListEntity updatedListEntity = DEF_LIST;
        updatedListEntity.setTitle(listDto.getTitle());
        updatedListEntity.setDescription(listDto.getDescription());

        ListDto expectedListDto = DEF_LIST_DTO;
        expectedListDto.setTitle(listDto.getTitle());
        expectedListDto.setDescription(listDto.getDescription());

        when(listRepository.findById(LIST_ID)).thenReturn(Optional.of(oldListEntity));
        when(listRepository.saveAndFlush(any(ListEntity.class))).thenReturn(updatedListEntity);
        when(listMapper.map(any(ListEntity.class))).thenReturn(expectedListDto);

        // Act
        Optional<ListDto> actualListDto = listService.update(LIST_ID, listDto);

        // Assert
        assertTrue(actualListDto.isPresent());
        assertEquals(expectedListDto, actualListDto.get());
        verify(listRepository, times(1)).findById(LIST_ID);
        verify(listRepository, times(1)).saveAndFlush(updatedListEntity);
        verify(listMapper, times(1)).map(updatedListEntity);
    }


}