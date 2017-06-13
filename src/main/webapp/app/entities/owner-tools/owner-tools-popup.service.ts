import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { OwnerTools } from './owner-tools.model';
import { OwnerToolsService } from './owner-tools.service';

@Injectable()
export class OwnerToolsPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private ownerToolsService: OwnerToolsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.ownerToolsService.find(id).subscribe((ownerTools) => {
                this.ownerToolsModalRef(component, ownerTools);
            });
        } else {
            return this.ownerToolsModalRef(component, new OwnerTools());
        }
    }

    ownerToolsModalRef(component: Component, ownerTools: OwnerTools): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.ownerTools = ownerTools;
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
