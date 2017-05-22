import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PermissionType } from './permission-type.model';
import { PermissionTypeService } from './permission-type.service';
@Injectable()
export class PermissionTypePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private permissionTypeService: PermissionTypeService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.permissionTypeService.find(id).subscribe((permissionType) => {
                this.permissionTypeModalRef(component, permissionType);
            });
        } else {
            return this.permissionTypeModalRef(component, new PermissionType());
        }
    }

    permissionTypeModalRef(component: Component, permissionType: PermissionType): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.permissionType = permissionType;
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
