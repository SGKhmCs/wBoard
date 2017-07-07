import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BoardsBody } from './boards-body.model';
import { BoardsBodyService } from './boards-body.service';

@Injectable()
export class BoardsBodyPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private boardsBodyService: BoardsBodyService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.boardsBodyService.find(id).subscribe((boardsBody) => {
                this.boardsBodyModalRef(component, boardsBody);
            });
        } else {
            return this.boardsBodyModalRef(component, new BoardsBody());
        }
    }

    boardsBodyModalRef(component: Component, boardsBody: BoardsBody): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.boardsBody = boardsBody;
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
