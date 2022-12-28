install
	node.js
	vscode

#install angular dev tool
npm install -g @angular/cli

#create new project
ng new HelloWorld
--routing N
--SCSS

#start web
npm start

angular.json => 定義一些檔案路徑
package.json => 打包設定檔，部分簡化指令(ex. npm start -> ng serve)也在裡面
src/app/app.component.html => HTML
app.component.scss => CSS
app.component.spec.ts => unit test
src/app/app.component.ts => 
	selector => APP的名字，對應到src/index.html的<app-root></app-root>
	templateUrl => html檔案
	styleUrls => css檔案
'
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'HelloWorld';
}
'


#install package
	Angular Language Service => 跳出提示function
	Angular Snippets (Version 13) => 提示tag
	Auto Import => auto import package
	Auto Rename Tag => rename tag
	ESLint => 提醒程式碼哪裡沒寫好
	
	
#ex. 讀取靜態檔案
1. 放到src\assets下
	因為angular.json裡的assets有設定那些會被讀進去
2. angular.json裡的assets新增路徑後重啟
	訪問方式 http://localhost:4200/xxx/xxx.html
	
#宣告全域變數
tsconfig.app.json 中有設定include src/**/*.d.ts的檔案，所以可以自己建立一個xxx.d.ts來宣告全域變數

