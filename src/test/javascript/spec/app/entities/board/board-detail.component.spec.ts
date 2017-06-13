import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BoardDetailComponent } from '../../../../../../main/webapp/app/entities/board/board-detail.component';
import { BoardService } from '../../../../../../main/webapp/app/entities/board/board.service';
import { Board } from '../../../../../../main/webapp/app/entities/board/board.model';

describe('Component Tests', () => {

    describe('Board Management Detail Component', () => {
        let comp: BoardDetailComponent;
        let fixture: ComponentFixture<BoardDetailComponent>;
        let service: BoardService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [BoardDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BoardService,
                    EventManager
                ]
            }).overrideTemplate(BoardDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BoardDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BoardService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Board(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.board).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
