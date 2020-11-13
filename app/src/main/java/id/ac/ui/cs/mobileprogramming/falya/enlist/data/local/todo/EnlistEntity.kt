package id.ac.ui.cs.mobileprogramming.falya.enlist.data.local.todo

data class EnlistEntity(var id: String,
                      var todo: String,
                      var completed: Boolean,
                      var date: String,
                      val user: String,
                      var createdAt: String)