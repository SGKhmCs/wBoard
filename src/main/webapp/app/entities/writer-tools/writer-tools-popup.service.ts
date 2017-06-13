import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { WriterTools } from './writer-tools.model';
import { WriterToolsService } from './writer-tools.service';

@Injectable()
export class WriterToolsPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private writerToolsService: WriterToolsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.writerToolsService.find(id).subscribe((writerTools) => {
                this.writerToolsModalRef(component, writerTools);
            });
        } else {
            return this.writerToolsModalRef(component, new WriterTools());
        }
    }

    writerToolsModalRef(component: Component, writerTools: WriterTools): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.writerTools = writerTools;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
