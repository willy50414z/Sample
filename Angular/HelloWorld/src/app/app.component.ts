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
}
