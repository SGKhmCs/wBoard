import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Admin } from './admin.model';
import { AdminPopupService } from './admin-popup.service';
import { AdminService } from './admin.service';
import { Board, BoardService } from '../board';
import { Writer, WriterService } from '../writer';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-admin-dialog',
    templateUrl: './admin-dialog.component.html'
})
export class AdminDialogComponent implements OnInit {

    admin: Admin;
    authorities: any[];
    isSaving: boolean;

    boards: Board[];

    writers: Writer[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private adminService: AdminService,
        private boardService: BoardService,
        private writerService: WriterService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.boardService.query()
            .subscribe((res: ResponseWrapper) => { this.boards = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.writerService
            .query({filter: 'admin-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.admin.writer || !this.admin.writer.id) {
                    this.writers = res.json;
                } else {
                    this.writerService
                        .find(this.admin.writer.id)
                        .subscribe((subRes: Writer) => {
                            this.writers = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.admin.id !== undefined) {
            this.subscribeToSaveResponse(
                this.adminService.update(this.admin));
        } else {
            this.subscribeToSaveResponse(
                this.adminService.create(this.admin));
        }
    }

    private subscribeToSaveResponse(result: Observable<Admin>) {
        result.subscribe((res: Admin) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Admin) {
        this.eventManager.broadcast({ name: 'adminListModification', content: 'OK'});
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

    trackBoardById(index: number, item: Board) {
        return item.id;
    }

    trackWriterById(index: number, item: Writer) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-admin-popup',
    template: ''
})
export class AdminPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adminPopupService: AdminPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.adminPopupService
                    .open(AdminDialogComponent, params['id']);
            } else {
                this.modalRef = this.adminPopupService
                    .open(AdminDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
