import { ShapeFactory } from './factories/shape.factory'
import { BazenFactory, FontanaFactory, StoFactory, StolicaFactory, ZeleninoFactory } from './factories/factories'
import { Component, Input } from '@angular/core';
import { Shape } from './shape'
import { CdkDragEnd } from '@angular/cdk/drag-drop';
import * as uuid from 'uuid';

@Component({
  selector: 'app-canvas',
  templateUrl: './canvas.component.html',
  styleUrls: ['./canvas.component.css']
})
export class CanvasComponent {

  shapes: Shape[] = []

  error_msg: string = "";

  @Input("type")
  public typeId : number = 1;

  readonly CANVAS_SIZE = { width: 500, height: 300 };

  pictureTitles: string[][] = [
    ["zelenino", "bazen"],
    ["zelenino", "fontana", "sto", "stolica"]
  ]
  factories: ShapeFactory[][] = [
    [new ZeleninoFactory(), new BazenFactory()],
    [new ZeleninoFactory(), new FontanaFactory(), new StoFactory(), new StolicaFactory()],
  ]

  dragEnd($event: CdkDragEnd, shape: Shape) {
    const position = $event.source.getFreeDragPosition();
    shape.x = position.x;
    shape.y = position.y;
  }

  create(shapeIndex: number) {
    const newShape: Shape = this.factories[this.typeId-1][shapeIndex].create(this.pictureTitles[this.typeId-1][shapeIndex]);
    this.shapes.push(newShape);
  }

  save(): void {
    //Svaka sa svakim bmk za O(n^2) ne mogu da razbijam glavu 

    if (this.shapes.length == 0) {
      this.error_msg = "Canvas je prazan";
      return;
    }

    for (let i = 0; i < this.shapes.length; i++) {
      const shapeA = this.shapes[i];
      if (shapeA.x < 0 || shapeA.x + shapeA.width > this.CANVAS_SIZE.width || shapeA.y < -this.CANVAS_SIZE.height || shapeA.y + shapeA.height > 0) {
        this.error_msg = "Neki oblik izlazi sa canvasa";
        return;
      }

      for (let j = i + 1; j < this.shapes.length; j++) {
        const shapeB = this.shapes[j];

        if (shapeA.x < shapeB.x+shapeB.width && shapeA.x+shapeA.width > shapeB.x &&
          shapeA.y+shapeA.height > shapeB.y && shapeA.y < shapeB.y+shapeB.height) {
          this.error_msg = "Postoje oblici koji se preklapaju";
          return;
        }
      }
    }

    var a = document.createElement("a");
    var file = new Blob([JSON.stringify(this.shapes)], { type: "text/plain" });
    a.href = URL.createObjectURL(file);
    a.download = "basta_" + uuid.v4() + ".json";
    a.click();

    this.error_msg = "";
  }

  file: any;
  fileChanged(e: any) {
    let fileReader = new FileReader();
    fileReader.onload = (e) => {
      try{
        if(fileReader.result) this.shapes = JSON.parse(fileReader.result.toString()) as Shape[];
      }catch(error){
        this.error_msg = "Fajl je neispravan";
      }
    }
    fileReader.readAsText(e.target.files[0]);
  }
}
