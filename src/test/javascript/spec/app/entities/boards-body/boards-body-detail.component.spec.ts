import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BoardsBodyDetailComponent } from '../../../../../../main/webapp/app/entities/boards-body/boards-body-detail.component';
import { BoardsBodyService } from '../../../../../../main/webapp/app/entities/boards-body/boards-body.service';
import { BoardsBody } from '../../../../../../main/webapp/app/entities/boards-body/boards-body.model';

describe('Component Tests', () => {

    describe('BoardsBody Management Detail Component', () => {
        let comp: BoardsBodyDetailComponent;
        let fixture: ComponentFixture<BoardsBodyDetailComponent>;
        let service: BoardsBodyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [BoardsBodyDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BoardsBodyService,
                    EventManager
                ]
            }).overrideTemplate(BoardsBodyDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BoardsBodyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BoardsBodyService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new BoardsBody(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.boardsBody).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
