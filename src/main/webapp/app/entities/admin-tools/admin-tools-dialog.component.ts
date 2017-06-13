import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { AdminTools } from './admin-tools.model';
import { AdminToolsPopupService } from './admin-tools-popup.service';
import { AdminToolsService } from './admin-tools.service';
import { User, UserService } from '../../shared';
import { Board, BoardService } from '../board';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-admin-tools-dialog',
    templateUrl: './admin-tools-dialog.component.html'
})
export class AdminToolsDialogComponent implements OnInit {

    adminTools: AdminTools;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    boards: Board[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private adminToolsService: AdminToolsService,
        private userService: UserService,
        private boardService: BoardService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.boardService.query()
            .subscribe((res: ResponseWrapper) => { this.boards = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.adminTools.id !== undefined) {
            this.subscribeToSaveResponse(
                this.adminToolsService.update(this.adminTools), false);
        } else {
            this.subscribeToSaveResponse(
                this.adminToolsService.create(this.adminTools), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<AdminTools>, isCreated: boolean) {
        result.subscribe((res: AdminTools) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AdminTools, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'wBoardApp.adminTools.created'
            : 'wBoardApp.adminTools.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'adminToolsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackBoardById(index: number, item: Board) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-admin-tools-popup',
    template: ''
})
export class AdminToolsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adminToolsPopupService: AdminToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.adminToolsPopupService
                    .open(AdminToolsDialogComponent, params['id']);
            } else {
                this.modalRef = this.adminToolsPopupService
                    .open(AdminToolsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
