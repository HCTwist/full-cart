package uk.henrytwist.fullcart.models

import java.time.LocalDateTime

class NewShoppingItem(val listId: Int, val name: String, val category: Category?, val quantity: Quantity, val checked: Boolean, val checkedTime: LocalDateTime?)