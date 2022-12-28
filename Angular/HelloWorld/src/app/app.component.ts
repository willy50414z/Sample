import { Component, OnInit } from '@angular/core';

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
}