import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AdminTools } from './admin-tools.model';
import { AdminToolsService } from './admin-tools.service';
@Injectable()
export class AdminToolsPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private adminToolsService: AdminToolsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.adminToolsService.find(id).subscribe((adminTools) => {
                this.adminToolsModalRef(component, adminTools);
            });
        } else {
            return this.adminToolsModalRef(component, new AdminTools());
        }
    }

    adminToolsModalRef(component: Component, adminTools: AdminTools): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.adminTools = adminTools;
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
