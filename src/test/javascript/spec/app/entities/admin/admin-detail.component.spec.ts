import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AdminDetailComponent } from '../../../../../../main/webapp/app/entities/admin/admin-detail.component';
import { AdminService } from '../../../../../../main/webapp/app/entities/admin/admin.service';
import { Admin } from '../../../../../../main/webapp/app/entities/admin/admin.model';

describe('Component Tests', () => {

    describe('Admin Management Detail Component', () => {
        let comp: AdminDetailComponent;
        let fixture: ComponentFixture<AdminDetailComponent>;
        let service: AdminService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [AdminDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AdminService,
                    EventManager
                ]
            }).overrideComponent(AdminDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AdminDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AdminService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Admin(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.admin).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
