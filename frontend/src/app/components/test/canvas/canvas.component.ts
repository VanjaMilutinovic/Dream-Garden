import { Component, VERSION, ViewChild, ViewContainerRef, ComponentFactoryResolver } from '@angular/core';

@Component({
  selector: 'app-canvas',
  templateUrl: './canvas.component.html',
  styleUrls: ['./canvas.component.css']
})
export class CanvasComponent {

  name = 'Angular ' + VERSION.major;
  componentRef: any;
  count = 0;
  @ViewChild('ele', { read: ViewContainerRef }) entry !: ViewContainerRef;


  create(event: any) {
    console.log(event)
    this.count++;
    this.componentRef.instance.text = "Draggble" + this.count;
  }
  dropped() {
    alert("hi")
  }

}
