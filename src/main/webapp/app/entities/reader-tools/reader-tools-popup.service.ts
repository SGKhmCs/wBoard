import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ReaderTools } from './reader-tools.model';
import { ReaderToolsService } from './reader-tools.service';
@Injectable()
export class ReaderToolsPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private readerToolsService: ReaderToolsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.readerToolsService.find(id).subscribe((readerTools) => {
                this.readerToolsModalRef(component, readerTools);
            });
        } else {
            return this.readerToolsModalRef(component, new ReaderTools());
        }
    }

    readerToolsModalRef(component: Component, readerTools: ReaderTools): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.readerTools = readerTools;
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
