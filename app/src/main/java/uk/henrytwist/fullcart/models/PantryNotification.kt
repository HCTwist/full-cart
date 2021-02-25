package uk.henrytwist.fullcart.models

class PantryNotification(val listId: Int, val listName: String, val items: List<Item>) {

    class Item(val name: String, val useByDate: UseByDate)
}