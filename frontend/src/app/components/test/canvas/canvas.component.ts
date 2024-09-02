import { ShapeFactory } from './factories/shape.factory'
import { BazenFactory, FontanaFactory,StoFactory,StolicaFactory,ZeleninoFactory } from './factories/factories'
import { Component } from '@angular/core';
import { Shape } from './shape'
import { CdkDragEnd } from '@angular/cdk/drag-drop';
import * as uuid from 'uuid';

@Component({
  selector: 'app-canvas',
  templateUrl: './canvas.component.html',
  styleUrls: ['./canvas.component.css']
})
export class CanvasComponent {

  shapes : Shape[] = []

  error_msg : string = "";

  readonly CANVAS_SIZE = {width: 500, height: 300};

  pictureTitles : string[] = ["zelenino", "bazen", "fontana", "sto", "stolica"]
  factories : ShapeFactory[] = [new ZeleninoFactory(), new BazenFactory(), new FontanaFactory(), new StoFactory(), new StolicaFactory()]

  dragEnd($event: CdkDragEnd, shape: Shape) {
    const position = $event.source.getFreeDragPosition();
    shape.x = position.x;
    shape.y = position.y;
  }

  create(shapeIndex : number){
    const newShape : Shape = this.factories[shapeIndex].create(this.pictureTitles[shapeIndex]);
    this.shapes.push(newShape);
  }

  save() : void{
    //Svaka sa svakim bmk za O(n^2) ne mogu da razbijam glavu 

    if(this.shapes.length == 0){
      this.error_msg = "Canvas je prazan";
      return;
    }

    for(let i = 0; i < this.shapes.length;i++){
      const shapeA = this.shapes[i];
      if(shapeA.x < 0 || shapeA.x+shapeA.width > this.CANVAS_SIZE.width || shapeA.y < -this.CANVAS_SIZE.height || shapeA.y+shapeA.height > 0){
        this.error_msg = "Neki oblik izlazi sa canvasa";
        return;
      }

      const RectA = shapeA.getBounds();

      for(let j = i+1; j < this.shapes.length;j++){
        const shapeB = this.shapes[j];
        const RectB = shapeB.getBounds();

        if (RectA.X1 < RectB.X2 && RectA.X2 > RectB.X1 &&
          RectA.Y1 > RectB.Y2 && RectA.Y2 < RectB.Y1) {
            this.error_msg = "Postoje oblici koji se preklapaju";
            return;
          }
      }
    }
    // let theJSON = JSON.stringify(this.shapes);
    // let uri = "data:application/json;charset=UTF-8," + encodeURIComponent(theJSON);

    // let a = document.createElement('a');
    // a.href = uri;
    // a.click();

    // let myBlob = new Blob([new Uint8Array(JSON.stringify(this.shapes))], {type: "octet/stream"});
    // var link = document.createElement('a');
    // link.href = window.URL.createObjectURL(myBlob);
    // link.click(); 
    var a = document.createElement("a");
    var file = new Blob([JSON.stringify(this.shapes)], {type: "text/plain"});
    a.href = URL.createObjectURL(file);
    a.download = "basta_"+uuid.v4()+".json";
    a.click();

    this.error_msg = "";
  }

}
