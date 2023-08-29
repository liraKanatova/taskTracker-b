package peaksoft.house.tasktrackerb9.services;

import peaksoft.house.tasktrackerb9.dto.request.ItemRequest;
import peaksoft.house.tasktrackerb9.dto.response.ItemResponse;
import peaksoft.house.tasktrackerb9.dto.response.SimpleResponse;

public interface ItemService {

     ItemResponse saveItem(ItemRequest itemRequest);

     ItemResponse addToCompletedAndRemoveFromCompleted(Long itemId);

     SimpleResponse deleteItem(Long itemId);

}