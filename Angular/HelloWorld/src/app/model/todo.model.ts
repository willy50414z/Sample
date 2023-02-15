//接收後端轉換成固定型別使用
export interface Todo{
    status:boolean,
    thing:string;
}
//前端需要建立物件使用
export class TodoClass{
    status = false;
    thing:string = '';
    constructor(_thing:string, _status:boolean = false) {
        this.thing = _thing
        this.status = _status
    }
}
export enum DisplayType{
    ALL,
    Active,
    Inactive
}