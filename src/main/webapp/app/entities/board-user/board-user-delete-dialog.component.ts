import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { BoardUser } from './board-user.model';
import { BoardUserPopupService } from './board-user-popup.service';
import { BoardUserService } from './board-user.service';

@Component({
    selector: 'jhi-board-user-delete-dialog',
    templateUrl: './board-user-delete-dialog.component.html'
})
export class BoardUserDeleteDialogComponent {

    boardUser: BoardUser;

    constructor(
        private boardUserService: BoardUserService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.boardUserService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'boardUserListModification',
                content: 'Deleted an boardUser'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-board-user-delete-popup',
    template: ''
})
export class BoardUserDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private boardUserPopupService: BoardUserPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.boardUserPopupService
                .open(BoardUserDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
