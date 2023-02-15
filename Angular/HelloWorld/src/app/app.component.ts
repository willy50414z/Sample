import { Component, OnInit } from '@angular/core';
import { Todo, TodoClass } from './model/todo.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  ngOnInit(): void {
    $("#bu").on("click", function(){
      alert('click');
    })
  }
  title = 'HelloWorld';
  placeholderProp = "placeholderValue"
  onSave(event:MouseEvent){
    alert("save success")
    alert(event.clientX)
  }

  checkboxAll = false
  checkbox1 = false
  checkbox2 = false
  toggleAll(){
    this.checkboxAll = !this.checkboxAll;
    this.checkbox1 = this.checkboxAll
    this.checkbox2 = this.checkboxAll
  }
  toggleCheck1(){
    this.checkbox1 = !this.checkbox1
  }
  toggleCheck2(){
    this.checkbox2 = !this.checkbox2
  }

  bgColor="red"
  width="10"
  changeWidth(input:HTMLInputElement){
    this.width = input.value
  }
  enterEvent(event:KeyboardEvent){
    console.log(event.key)
  }

  toDoList: Todo[] = [
    {
      status: true,
      thing:"thing1"
    },
    {
      status: true,
      thing:"thing2"
    },
    {
      status: false,
      thing:"thing3"
    }
  ];
  destroy(index:number){
    //從第{{index}}的位置刪除i個物件
    this.toDoList.splice(index,1)
  }
  addTodo(todoInput:HTMLInputElement){
    const todo:Todo = {
      status:true,
      thing:todoInput.value
    }
    this.toDoList.push(todo)
  }
  addTodoClass(todoInput:HTMLInputElement){
    this.toDoList.push(new TodoClass(todoInput.value))
  }

  ngModuleInput = ''

  date = new Date()
}
