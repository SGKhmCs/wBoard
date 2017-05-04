import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PermissionTypeDetailComponent } from '../../../../../../main/webapp/app/entities/permission-type/permission-type-detail.component';
import { PermissionTypeService } from '../../../../../../main/webapp/app/entities/permission-type/permission-type.service';
import { PermissionType } from '../../../../../../main/webapp/app/entities/permission-type/permission-type.model';

describe('Component Tests', () => {

    describe('PermissionType Management Detail Component', () => {
        let comp: PermissionTypeDetailComponent;
        let fixture: ComponentFixture<PermissionTypeDetailComponent>;
        let service: PermissionTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [PermissionTypeDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PermissionTypeService,
                    EventManager
                ]
            }).overrideComponent(PermissionTypeDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PermissionTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PermissionTypeService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PermissionType(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.permissionType).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
